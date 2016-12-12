precision mediump float;

uniform vec3 u_LightPos;
uniform vec3 u_lightColor;
uniform vec3 u_ViewPos;
uniform mat4 u_IT_MVMatrix;
uniform int u_NR_Point_Lights;

varying vec2 v_TextureCoordinates;
varying vec3 v_FragPos;
varying vec3 v_Normal;
varying vec3 v_NormTest;

struct Light {
    vec3 position;

    vec3 ambient;
    vec3 diffuse;
    vec3 specular;

    float constant;
    float linear;
    float quadratic;
};

uniform Light light[u_nr_point_lights];

struct Material {
    sampler2D diffuseTex;
    sampler2D specularTex;
    float     shininess;
};

uniform Material material;

void main()
{
    vec3 result = vec3(0.0f);
    for(int i = 0; i < u_nr_point_lights; i++) {
    float distance = length(light.position - FragPos);
    float attenuation = 1.0f / (light.constant + light.linear * distance + light.quadratic * (distance * distance));

    vec3 ambient = light.ambient * vec3(texture2D(material.diffuseTex, v_TextureCoordinates));

    // Diffuse
    vec3 norm = normalize(v_Normal);
    vec3 lightDir = normalize(light.position - v_FragPos);
    float diff = max(dot(norm, lightDir), 0.0);
    vec3 diffuse = light.diffuse * (diff * vec3(texture2D(material.diffuseTex, v_TextureCoordinates)));

    // Specular
    vec3 viewDir = normalize(u_ViewPos - v_FragPos);
    vec3 reflectDir = reflect(-lightDir, norm);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), material.shininess);
    vec3 specular = light.specular * (spec * vec3(texture2D(material.specularTex, v_TextureCoordinates)));

    ambient  *= attenuation;
    diffuse  *= attenuation;
    specular *= attenuation;
    result += (ambient + diffuse + specular);
    }
    gl_FragColor = vec4(result, 1.0f);


}
