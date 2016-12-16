#version 300 es
precision mediump float;
layout (location = 0) out vec4 FragColor;
layout (location = 1) out vec4 BrightColor;

in vec2 v_TextureCoordinates;

uniform sampler2D u_Texture;

void main() {
    FragColor = vec4(vec3(texture(u_Texture, v_TextureCoordinates).rgb * 0.2f),1.0f);
    BrightColor = vec4(0.0f);

}


