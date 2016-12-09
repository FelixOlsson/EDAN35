precision mediump float;

uniform vec3 u_LightPos;
uniform vec3 u_ViewPos;
uniform mat4 u_IT_MVMatrix;

varying vec2 v_TextureCoordinates;
varying vec3 v_FragPos;
varying vec3 v_Normal;
varying vec3 v_NormTest;

struct Material {
    sampler2D diffuseTex;
    sampler2D specularTex;
    float     shininess;
};

uniform Material material;

void main()
{
    vec3 ambient = vec3(texture2D(material.diffuseTex, v_TextureCoordinates));

    vec3 norm = normalize(v_Normal);
    vec3 lightDir = normalize(u_LightPos - v_FragPos);
    float diff = max(dot(norm, lightDir), 0.0);
    vec3 diffuse = diff * vec3(1.0f);


    float specularStrength = 3.5f;
    vec3 viewDir = normalize(u_ViewPos - v_FragPos);
    vec3 reflectDir = reflect(-lightDir, norm);

    float spec = pow(max(dot(viewDir, reflectDir), 0.0), 32.0f);
    vec3 specular = spec * vec3(texture2D(material.specularTex, v_TextureCoordinates));

    vec4 textureColor = texture2D(material.diffuseTex, vec2(v_TextureCoordinates.s, v_TextureCoordinates.t));
    gl_FragColor = vec4( (ambient + diffuse + specular) * textureColor.rgb, textureColor.a);


}
