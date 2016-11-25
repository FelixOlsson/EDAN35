precision mediump float;

uniform sampler2D u_TextureUnit;
varying vec2 v_TextureCoordinates;
varying vec3 v_Color;

void main()
{
    vec4 textureColor = texture2D(u_TextureUnit, vec2(v_TextureCoordinates.s, v_TextureCoordinates.t));
    gl_FragColor = textureColor;


}
