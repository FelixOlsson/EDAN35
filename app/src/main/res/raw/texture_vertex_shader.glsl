#version 300 es
uniform mat4 u_MVPMatrix;
uniform mat4 u_MMatrix;

in vec3 a_Normal;
in vec4 a_Position;
in vec2 a_TextureCoordinates;

out vec2 v_TextureCoordinates;
out vec3 v_FragPos;
out vec3 v_Normal;

void main()
{

    //v_Normal = mat3(u_NormalMatrix) *  a_Normal;
    v_Normal = mat3(transpose(inverse(u_MMatrix))) * a_Normal;
    v_TextureCoordinates = a_TextureCoordinates;
    gl_Position = u_MVPMatrix * a_Position;
    v_FragPos = vec3(u_MMatrix * a_Position);
}


