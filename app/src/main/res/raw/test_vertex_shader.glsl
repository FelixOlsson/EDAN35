#version 300 es
uniform mat4 u_MVPMatrix;

in vec4 a_Position;
in vec2 a_TextureCoordinates;

out vec2 v_TextureCoordinates;

void main() {
    v_TextureCoordinates = a_TextureCoordinates;
    gl_Position =  u_MVPMatrix * a_Position;
}
