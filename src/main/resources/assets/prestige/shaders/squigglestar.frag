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

vec2 random2(vec2 p) {
    return fract(sin(vec2(dot(p, vec2(127.1, 311.7)), dot(p, vec2(269.5, 183.3))))*43758.5453);
}
    #define NUM_OCTAVES 5


float random (in vec2 _st) {
    return fract(sin(dot(_st.xy,
    vec2(12.9898, 78.233)))*
    43758.5453123);
}

// Based on Morgan McGuire @morgan3d
// https://www.shadertoy.com/view/4dS3Wd
float noise (in vec2 _st) {
    vec2 i = floor(_st);
    vec2 f = fract(_st);

    // Four corners in 2D of a tile
    float a = random(i);
    float b = random(i + vec2(1.0, 0.0));
    float c = random(i + vec2(0.0, 1.0));
    float d = random(i + vec2(1.0, 1.0));

    vec2 u = f * f * (3.0 - 2.0 * f);

    return mix(a, b, u.x) +
    (c - a)* u.y * (1.0 - u.x) +
    (d - b) * u.x * u.y;
}
float fbm (in vec2 _st) {
    float v = 0.0;
    float a = 0.5;
    vec2 shift = vec2(100.0);
    // Rotate to reduce axial bias
    mat2 rot = mat2(cos(0.5), sin(0.5),
    -sin(0.5), cos(0.50));
    for (int i = 0; i < NUM_OCTAVES; ++i) {
        v += a * noise(_st);
        _st = rot * _st * 2.0 + shift;
        a *= 0.5;
    }
    return v;
}


void main() {
    vec2 st = gl_FragCoord.xy/resolution.xy;
    st.x *= resolution.x/resolution.y;
    st.x += offsetX /80.;
    st.y += -offsetY/80.;

    st.x = fbm(st);
    st.y = fbm(st + vec2(1.0));

    vec3 color = vec3(.0);

    st *= 6.;

    vec2 i_st = floor(st);
    vec2 f_st = fract(st);

    float m_dist = 0.5;

    for (int y= -1; y <= 1; y++) {
        for (int x= -1; x <= 1; x++) {
            vec2 neighbor = vec2(float(x), float(y));

            vec2 point = random2(i_st + neighbor);

            point = 0.5*sin(time/15. + 32*point);

            vec2 diff = neighbor + point - f_st;

            float dist = length(diff);

            m_dist = min(m_dist, dist);
        }
    }

    color += m_dist*1.2;
    vec4 col = vec4(color * vec3(0.2, 0.2, 0.2), 1);
    float tolerance = 0.1;
    if (col.r >=tolerance && col.g >=tolerance && col.b >=tolerance){
        col.r *= 1.5;
        col.b *= 1.5;
    } else {
        col.b *= 2;
    }
    gl_FragColor = col;
}