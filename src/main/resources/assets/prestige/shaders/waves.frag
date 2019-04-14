#version 120

varying vec3 vertexPos;

uniform sampler2D bgl_RenderedTexture;
uniform sampler2D image;
uniform sampler2D mask;
uniform float time;

uniform float perc;
uniform vec3 resolution;

uniform float offsetX;
uniform float offsetY;




const float PI = 3.1415926535979;

//////// Matrix math library
vec2 rotate(vec2 p, float t)
{
    mat2 m = mat2(cos(t), -sin(t), sin(t), cos(t));
    return m*p;
}

//////// Shape library
float d_sphere(vec2 p, float radius)
{
    return length(p) - radius;
}

float d_sinewave(vec2 p)
{
    float time = (time/15.);
    p.x -=
    //(.8+.25*sin(time))*
    p.y;// bent forward
    return p.y - sin(p.x);
}

float chopped_wave(vec2 p)
{
    p *= 8.0;

    vec2 pm = p;
    pm += vec2(-3.3, 0.1);
    pm.x += 2.0;
    pm.x = mod(pm.x, 2.*PI);
    pm.x -= 2.0;
    return
    -min(
    -d_sinewave(p),
    d_sphere(pm*vec2(.5, 1.), 0.95)
    );
}

float dorky_dolphin(vec2 p, float radius, float rot)
{
    p.y += .75;
    vec2 pp = vec2(length(p), atan(p.y, p.x));
    float t = -rot+-2.2+2.*pp.y + 0.5*pp.x;
    float r = radius
    + 0.35*radius
    * (1.-fract(t))
    * (step(1., t)-step(2., t));
    return max(pp.x-r, -p.y);
}


//////// Colorization
vec3 wave_color(float d, float s, vec2 uv)
{
    float b = 0.001;
    // border
    float bw = 0.08;
    float innergray =
    mix(.5, 0., smoothstep(-0.01, 0.01, d))
    + mix(.0, 1., smoothstep(bw, 2.*bw, d));
    vec3 blue = vec3(
    vec2(s)
    + 0.2, 1.);
    vec3 white = vec3(1.);
    return mix(white, blue, 2.*innergray);
}

vec2 getRoll(
in vec2 uv,
in float xscale, in float yoff,
in vec2 bAmpl, in float bFreq)
{
    float time = (time/15.);
    uv.x += bAmpl.x*sin(bFreq*time);
    uv.y += bAmpl.y*cos(bFreq*time);
    uv.x *= xscale;
    uv.y += yoff;
    return uv;
}

vec3 sunset(in vec2 uv)
{
    float time = 1;;
    float sunh = sin(
    0.005*
    time);
    vec2 sunpos = vec2(.5, .2-sunh);
    vec3 hot = vec3(1., 1., 1.);
    vec3 orange = vec3(1., .65, 0.);
    vec3 red = vec3(.5, 0., 0.);
    float d = length(uv-sunpos);
    float f = clamp(pow(1.3-d, 3.), .0, 1.);
    return mix(orange, hot, f)
    + mix(red, orange, uv.y-4.*sunh);
}

vec3 getColorFromUV(in vec2 uv)
{
    vec2 q = getRoll(uv, 1.7, 0.5, vec2(0.3, 0.1), 2.1);
    float dist = chopped_wave(q);
    float shade = 0.0;
    if (dist > 0.1)
    {
        q = getRoll(1.5*uv, 1.7, 0.35, vec2(0.1, 0.05), 3.1);
        dist = chopped_wave(q);
        shade = 0.22;
    }
    if (dist > 0.1)
    {
        float time = (time/15.);
        vec2 dof = vec2(4.*fract(0.1*time)-2., 0.2*sin(PI*time)+0.15);
        dist = dorky_dolphin(uv+dof, 0.45, 3.*fract(time)-1.6);
        if (dist < 0.1)
        return vec3(0.5);
    }
    if (dist > 0.1)
    {
        q = getRoll(1.85*uv+vec2(.5, 0.), 1.7, 0.15, vec2(0.08, 0.04), 4.1);
        dist = chopped_wave(q);
        shade = 0.35;
    }
    if (dist > 0.1)
    {
        q = getRoll(2.8*uv*vec2(1., 1.5)+vec2(.5, 0.), 2.2, -0.0, vec2(0.08, 0.052), 3.1);
        dist = chopped_wave(q);
        shade = 0.5;
    }
    if (dist > 0.1)
    {
        return sunset(uv);
    }
    else return wave_color(dist, shade, q);
}

////////
void main()
{
    vec2 uv = gl_FragCoord.xy / resolution.xy;
//    uv = += vec2(offsetX/80., -offsetY/80.);
    // Fit [-1,1] into screen and expand for aspect ratio
    vec2 uv11 = 2.0*uv - vec2(1.0, 1.0);
    float aspect = resolution.x / resolution.y;
    if (aspect > 1.0) uv11.x *= aspect;
    else uv11.y /= aspect;

    vec2 center = vec2(0.0, 0.0);
    uv11 -= center;
    vec3 col = getColorFromUV(uv11);
    vec2 q = uv;
    col *= 0.3 + 0.5*pow( 16.0*q.x*q.y*(1.0-q.x)*(1.0-q.y), 0.1 );

    gl_FragColor = vec4(col, 1.0);
}
