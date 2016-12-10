attribute vec2 position;
attribute vec2 texCoords;

varying vec2 TexCoords;

void main()
{
    vec2 madd= vec2(0.5,0.5);
    gl_Position = vec4(position.x, position.y, 0.0f, 1.0f);
    TexCoords = texCoords*madd+madd;
}