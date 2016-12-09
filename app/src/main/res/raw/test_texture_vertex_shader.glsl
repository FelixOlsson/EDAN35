uniform mat4 u_MVPMatrix;
uniform mat4 u_MMatrix;
uniform mat4 u_NormalMatrix;

attribute vec3 a_Normal;
attribute vec4 a_Position;
attribute vec2 a_TextureCoordinates;

varying vec2 v_TextureCoordinates;
varying vec3 v_FragPos;
varying vec3 v_Normal;
varying vec3 v_NormTest;

void main()
{

    //v_Normal = mat3(u_NormalMatrix) *  a_Normal;
    v_Normal = mat3(transpose(inverse(u_MMatrix))) * a_Normal;
    v_TextureCoordinates = a_TextureCoordinates;
    gl_Position = u_MVPMatrix * a_Position;
    v_FragPos = vec3(u_MMatrix * a_Position);
}


