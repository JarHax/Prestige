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

void main() {
    vec2 st = gl_FragCoord.xy/resolution.xy;
    st.x *= resolution.x/resolution.y;
    st.x += offsetX /80.;
    st.y += -offsetY/80.;
    vec3 color = vec3(.0);

    st *= 6.;

    vec2 i_st = floor(st);
    vec2 f_st = fract(st);

    float m_dist = 1;

    for (int y= -1; y <= 1; y++) {
        for (int x= -1; x <= 1; x++) {
            vec2 neighbor = vec2(float(x), float(y));

            vec2 point = random2(i_st + neighbor);

            point = 0.5*sin(time/15. + 32*point);

            vec2 diff = neighbor + point;

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