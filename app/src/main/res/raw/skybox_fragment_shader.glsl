#version 300 es
precision mediump float;
layout (location = 0) out vec4 FragColor;
layout (location = 1) out vec4 BrightColor;

uniform samplerCube u_TextureUnit;
in vec3 v_Position;
	    	   								
void main()                    		
{

	FragColor = texture(u_TextureUnit, v_Position);
	BrightColor = vec4(0.0f);

}