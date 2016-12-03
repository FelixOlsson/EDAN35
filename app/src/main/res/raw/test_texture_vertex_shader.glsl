uniform mat4 u_MVPMatrix;
//uniform vec3 u_VectorToLight;

attribute vec3 a_Normal;
attribute vec4 a_Position;
attribute vec2 a_TextureCoordinates;

varying vec2 v_TextureCoordinates;
varying vec3 v_Color;

void main()
{
    vec3 scaledNormal = a_Normal;
    scaledNormal.y *= 10.0;
    scaledNormal = normalize(scaledNormal);
    //float diffuse = max(dot(scaledNormal, u_VectorToLight), 0.0);
    //v_Color *= diffuse;
    v_TextureCoordinates = a_TextureCoordinates;
    gl_Position = u_MVPMatrix * a_Position;
}
