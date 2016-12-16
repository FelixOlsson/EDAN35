#version 300 es

uniform mat4 projection;
uniform mat4 view;
in vec3 a_Position;
out vec3 v_Position;

void main()                    
{                                	  	          

	           
    gl_Position = projection *  mat4(mat3(view)) *  vec4(a_Position, 1.0);
    gl_Position = gl_Position.xyww;
    v_Position = a_Position;
}    