#version 300 es

uniform mat4 u_Matrix;
uniform float u_Time;
uniform sampler2D u_Noise;

in vec3 a_Position;
in vec3 a_Color;
in vec3 a_DirectionVector;
in float a_ParticleStartTime;

out vec3 v_Color;
out float v_ElapsedTime;

void main()
{
    v_Color = a_Color;
    v_ElapsedTime = u_Time - a_ParticleStartTime;
    vec3 currentPos = a_Position + (a_DirectionVector * v_ElapsedTime);

    gl_PointSize = 10.0;
    gl_Position = u_Matrix * vec4(currentPos, 1.0);

}
