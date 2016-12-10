varying vec2 TexCoords;

uniform sampler2D screenTexture;

void main()
{
    gl_FragColor = vec4(vec3(1.0 - texture2D(screenTexture, TexCoords)), 1.0);
}