precision mediump float;
uniform sampler2D u_TextureUnit;
uniform sampler2D u_Noise;
varying vec3 v_Color;
varying float v_ElapsedTime;
void main()
{
    float random = (texture2D(u_Noise, gl_PointCoord * v_ElapsedTime + 0.5f).r + texture2D(u_Noise, gl_PointCoord * v_ElapsedTime + 0.5f).g +
    texture2D(u_Noise, gl_PointCoord * v_ElapsedTime + 0.5f).b + texture2D(u_Noise, gl_PointCoord * v_ElapsedTime + 0.5f).a);

    gl_FragColor = vec4( texture2D(u_TextureUnit, gl_PointCoord).xyz
    , 1.0) /  (random + v_ElapsedTime) ;


}

