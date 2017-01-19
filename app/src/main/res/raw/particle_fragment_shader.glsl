#version 300 es

precision mediump float;
uniform sampler2D u_TextureUnit;
uniform sampler2D u_Noise;
in vec3 v_Color;
in float v_ElapsedTime;

out vec4 gl_FragColor;

float rand(vec2 co);

void main()
{


    float random = (texture(u_Noise, gl_PointCoord + v_ElapsedTime * 0.5f).r + texture(u_Noise, gl_PointCoord + v_ElapsedTime * 0.5f).g +
    texture(u_Noise, gl_PointCoord + v_ElapsedTime * 0.5f).b + texture(u_Noise, gl_PointCoord + v_ElapsedTime * 0.5f).a);

    gl_FragColor = vec4( texture(u_TextureUnit, gl_PointCoord).xyz
    , 1.0) /  ((random * v_ElapsedTime) + v_ElapsedTime);


}

float rand(vec2 co)
{
   return fract(sin(dot(co.xy,vec2(12.9898,78.233))) * 43758.5453);
}

