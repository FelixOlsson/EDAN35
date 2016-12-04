precision mediump float;

uniform sampler2D u_TextureUnit;
uniform vec3 u_LightPos;

varying vec2 v_TextureCoordinates;
varying vec3 v_FragPos;
varying vec3 v_Normal;

void main()
{
    vec3 norm = normalize(v_Normal);
    vec3 lightDir = normalize(u_LightPos - v_FragPos);

    float diff = max(dot(norm, lightDir), 0.0);
    vec3 diffuse = diff * vec3(1.0f);

    float ambient = 0.3f;
    vec4 textureColor = texture2D(u_TextureUnit, vec2(v_TextureCoordinates.s, v_TextureCoordinates.t));
    gl_FragColor = vec4(textureColor.rgb * (ambient + diffuse), textureColor.a);


}
