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

#ifdef GL_ES
precision mediump float;
#endif

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

    #define NUM_OCTAVES 5

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
    vec2 st = gl_FragCoord.xy/resolution.xy*3.;
    // st += st * abs(sin(time*0.1)*3.0);
    vec2 texcoord = vec2(gl_TexCoord[0]);

    vec4 col = texture2D(bgl_RenderedTexture, texcoord);
    vec3 color = col.rgb;

    vec2 q = vec2(0.);
    q.x = fbm(st + 0.00 * time);
    q.y = fbm(st + vec2(1.0));

    vec2 r = vec2(0.);
    //add player offset to these after time, xz not y
    r.x = fbm(st + 1.0*q + vec2(1.7, 9.2)+ 0.15*time/15.) ;
    r.y = fbm(st + 1.0*q + vec2(8.3, 2.8)+ 0.126*time/15.);
    r.x +=+offsetX/80.;
    r.y +=-offsetY/80.;
    float f = fbm(st+r);

    color = mix(vec3(0.8, 0.0, 0.666667), vec3(0.666667, 0., 0.498039), clamp((f*f)*4.0, 0.0, 1.0));
    color = mix(color, vec3(0, 0, 0.164706), clamp(length(q), 0.0, 1.0));
    color = mix(color, vec3(0.8, 0.666667, 0.8), clamp(length(r.x), 0.0, 1.0));

    gl_FragColor = vec4((f*f*f+.6*f*f+.5*f)*color, 1);
}


