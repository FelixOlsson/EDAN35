uniform mat4 u_MVPMatrix;
uniform vec4 u_Color;
attribute vec4 a_Position;
attribute vec4 a_Color;
varying vec4 test;

void main() {
    test = u_Color;
    gl_Position =  u_MVPMatrix * a_Position;
}
