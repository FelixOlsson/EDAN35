uniform mat4 u_MVPMatrix;
uniform mat4 u_MMatrix;
uniform mat4 u_NormalMatrix;

attribute vec3 a_Normal;
attribute vec4 a_Position;
attribute vec2 a_TextureCoordinates;

varying vec2 v_TextureCoordinates;
varying vec3 v_FragPos;
varying vec3 v_Normal;

void main()
{
    v_Normal = mat3(u_NormalMatrix) *  a_Normal;
    v_TextureCoordinates = a_TextureCoordinates;
    gl_Position = u_MVPMatrix * a_Position;

    v_FragPos = vec3(u_MMatrix * a_Position);
}

mat3 mat3_emu(mat4 m4) {
  return mat3(
      m4[0][0], m4[0][1], m4[0][2],
      m4[1][0], m4[1][1], m4[1][2],
      m4[2][0], m4[2][1], m4[2][2]);
}
