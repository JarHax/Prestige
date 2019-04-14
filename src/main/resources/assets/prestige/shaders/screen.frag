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

void main() {
    vec2 point = vec2(gl_TexCoord[0]);
    vec4 color = texture2D(bgl_RenderedTexture, point);
    gl_FragColor = color;
}