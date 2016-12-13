#version 300 es
uniform vec3 u_Diffuse;
layout (location = 0) out vec4 FragColor;
layout (location = 1) out vec4 BrightColor;


void main() {
    //FragColor = vec4(u_Diffuse,1.0f);
    //float brightness = dot(FragColor.rgb, vec3(0.2126, 0.7152, 0.0722));
    //FragColor = vec4(0.9f);
    BrightColor = vec4(u_Diffuse, 1.0);
}
