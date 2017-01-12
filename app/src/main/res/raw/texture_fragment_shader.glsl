#version 300 es
precision mediump float;
layout (location = 0) out vec4 FragColor;
layout (location = 1) out vec4 BrightColor;

uniform vec3 u_ViewPos;

in vec2 v_TextureCoordinates;
in vec3 v_FragPos;
in vec3 v_Normal;

struct DirLight {
    vec3 direction;

    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
};

uniform DirLight dirLight;

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

vec3 CalcPointLight(Light light, vec3 normal, vec3 fragPos, vec3 viewDir);

void main()
{
    vec3 norm = normalize(v_Normal);
    vec3 viewDir = normalize(u_ViewPos - v_FragPos);
    vec3 result = vec3(0.0f);

    for(int i = 0; i < 3; i++) {
        result += CalcPointLight(light[i], norm, v_FragPos, viewDir) ;
    }


    FragColor = vec4(result, 1.0f);
}


vec3 CalcPointLight(Light light, vec3 normal, vec3 fragPos, vec3 viewDir)
{

    float distance = length(light.position - fragPos);
    float attenuation = 1.0f / (light.constant + light.linear * distance + light.quadratic * (distance * distance));

    vec3 surfaceToLightDir = normalize(light.position - fragPos);

    float brightnessLight = max(dot(normal, surfaceToLightDir), 0.0);

    vec3 reflectDir = reflect(-surfaceToLightDir, normal);
    float spec = max(dot(viewDir, reflectDir), 0.0);

    brightnessLight += spec;

    return vec3(brightnessLight * light.diffuse * texture(material.diffuseTex, v_TextureCoordinates).rgb * attenuation);
}

