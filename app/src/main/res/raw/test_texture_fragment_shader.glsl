precision mediump float;

uniform sampler2D u_TextureUnit;
varying vec2 v_TextureCoordinates;

void main()
{
    vec4 textureColor = texture2D(u_TextureUnit, vec2(v_TextureCoordinates.s, v_TextureCoordinates.t));
        gl_FragColor = vec4(textureColor.rgb, textureColor.a);

}
