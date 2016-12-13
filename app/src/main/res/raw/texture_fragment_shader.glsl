#version 300 es
precision mediump float;
layout (location = 0) out vec4 FragColor;
layout (location = 1) out vec4 BrightColor;

uniform vec3 u_ViewPos;

in vec2 v_TextureCoordinates;
in vec3 v_FragPos;
in vec3 v_Normal;

struct Light {
    vec3 position;

    vec3 ambient;
    vec3 diffuse;
    vec3 specular;

    float constant;
    float linear;
    float quadratic;
};

uniform Light light[3];

struct Material {
    sampler2D diffuseTex;
    sampler2D specularTex;
    float     shininess;
};

uniform Material material;

void main()
{
    vec3 result = vec3(0.0f);

    for(int i = 0; i < 3; i++) {
    float distance = length(light[i].position - v_FragPos);
    float attenuation = 1.0f / (light[i].constant + light[i].linear * distance + light[i].quadratic * (distance * distance));

    vec3 ambient = 1.0f * vec3(texture(material.diffuseTex, v_TextureCoordinates));

    // Diffuse
    vec3 norm = normalize(v_Normal);
    vec3 lightDir = normalize(light[i].position - v_FragPos);
    float diff = max(dot(norm, lightDir), 0.0);
    vec3 diffuse = light[i].diffuse * (diff * vec3(texture(material.diffuseTex, v_TextureCoordinates)));

    // Specular
    vec3 viewDir = normalize(u_ViewPos - v_FragPos);
    vec3 reflectDir = reflect(-lightDir, norm);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), material.shininess);
    vec3 specular = light[i].specular * (spec * vec3(texture(material.specularTex, v_TextureCoordinates)));

    ambient  *= attenuation;
    diffuse  *= attenuation;
    specular *= attenuation;
    result += (ambient + diffuse + specular);
    }

    float brightness = dot(result, vec3(0.2126, 0.7152, 0.0722));
    if(brightness > 1.0) {
           BrightColor = vec4(result, 1.0);
      }

    FragColor = vec4(result, 1.0f);




}
