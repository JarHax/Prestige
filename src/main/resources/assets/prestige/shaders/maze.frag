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

#define PI 3.14159265358979323846

float random (in vec2 _st) {
    return fract(sin(dot(_st.xy,
    vec2(12.9898, 78.233)))*
    43758.5453123);
}

vec2 truchetPattern(in vec2 _st, in float _index){
    _index = fract(((_index-0.5)*2.0));
    if (_index > 0.75) {
        _st = vec2(1.0) - _st;
    } else if (_index > 0.5) {
        _st = vec2(1.0-_st.x, _st.y);
    } else if (_index > 0.25) {
        _st = 1.0-vec2(1.0-_st.x, _st.y);
    }
    return _st;
}

void main() {
    vec2 st = gl_FragCoord.xy/resolution.xy;
    st *= 20.0;
    // st = (st-vec2(5.0))*(abs(sin(time*0.2))*5.);
    // st.x += time*3.0;

    st += vec2(offsetX/80., -offsetY/80.);
    vec2 ipos = floor(st);// integer
    vec2 fpos = fract(st);// fraction

    vec2 tile = truchetPattern(fpos, random(ipos));

    float color = 0.0;

    // Maze
    color = smoothstep(tile.x-0.3, tile.x, tile.y)-
    smoothstep(tile.x, tile.x+0.3, tile.y);

    // Circles
    color = (step(length(tile), 0.6) - step(length(tile), 0.4)) +
    (step(length(tile-vec2(1.)), 0.6) - step(length(tile-vec2(1.)), 0.4));

    // Truchet (2 triangles)
    // color = step(tile.x,tile.y);

    gl_FragColor = vec4(vec3(color*vec4(0.3, 0.3, 0.8, 1)), 1.0);
}