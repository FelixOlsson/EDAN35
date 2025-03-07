#version 300 es
out vec4 FragColor;
in vec2 TexCoords;

uniform sampler2D image;

uniform bool horizontal;



void main()
{
    float weight[5] = float[] (0.227027, 0.1945946, 0.1216216, 0.054054, 0.016216);
    vec2 tex_offset = vec2(1 / textureSize(image, 0)); // gets size of single texel
    vec3 result = texture(image, TexCoords).rgb * weight[0]; // current fragment's contribution
    if(horizontal)
    {
        for(int i = 1; i < 5; ++i)
        {
            result += texture(image, TexCoords + vec2(tex_offset.x * float(i), 0.0)).rgb * weight[i];
            result += texture(image, TexCoords - vec2(tex_offset.x * float(i), 0.0)).rgb * weight[i];
        }
    }
    else
    {
        for(int i = 1; i < 5; ++i)
        {
            result += texture(image, TexCoords + vec2(0.0, tex_offset.y * float(i))).rgb * weight[i];
            result += texture(image, TexCoords - vec2(0.0, tex_offset.y * float(i))).rgb * weight[i];
        }
    }
    FragColor = vec4(result, 1.0);
}