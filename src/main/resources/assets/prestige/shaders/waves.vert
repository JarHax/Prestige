varying vec4 vertColor;

varying vec3 vertexPos;

void main(){
    gl_Position = gl_ModelViewProjectionMatrix*gl_Vertex;
    vertexPos = gl_Vertex.xyz;
}