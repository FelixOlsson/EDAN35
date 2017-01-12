precision mediump float;

uniform sampler2D u_TextureUnit;
uniform sampler2D u_noise;
//uniform float u_Time;

//varying vec3 v_Color;
varying float v_ElapsedTime;

void main()
{
    // Generate noisy x value
    vec2 n0Uv = vec2(gl_PointCoord.x*1.4 + 0.01, gl_PointCoord.y + v_ElapsedTime*0.69*10.0);
    vec2 n1Uv = vec2(gl_PointCoord.x*0.5 - 0.033, gl_PointCoord.y*2.0 + v_ElapsedTime*0.12*10.0);
    vec2 n2Uv = vec2(gl_PointCoord.x*0.94 + 0.02, gl_PointCoord.y*3.0 + v_ElapsedTime*0.61*10.0);
    float n0 = (texture2D(u_TextureUnit, n0Uv).w-0.5)*2.0;
    float n1 = (texture2D(u_TextureUnit, n1Uv).w-0.5)*2.0;
    float n2 = (texture2D(u_TextureUnit, n2Uv).w-0.5)*2.0;
    float noiseA = clamp(n0 + n1 + n2, -1.0, 1.0);

    // Generate noisy y value
    vec2 n0UvB = vec2(gl_PointCoord.x*0.7 - 0.01, gl_PointCoord.y + v_ElapsedTime*0.27*10.0);
    vec2 n1UvB = vec2(gl_PointCoord.x*0.45 + 0.033, gl_PointCoord.y*1.9 + v_ElapsedTime*0.61*10.0);
    vec2 n2UvB = vec2(gl_PointCoord.x*0.8 - 0.02, gl_PointCoord.y*2.5 + v_ElapsedTime*0.51*10.0);
    float n0B = (texture2D(u_TextureUnit, n0UvB).w-0.5)*2.0;
    float n1B = (texture2D(u_TextureUnit, n1UvB).w-0.5)*2.0;
    float n2B = (texture2D(u_TextureUnit, n2UvB).w-0.5)*2.0;
    float noiseB = clamp(n0B + n1B + n2B, -1.0, 1.0);

    vec2 finalNoise = vec2(noiseA, noiseB);
    float perturb = (1.0 - gl_PointCoord.y) * 0.35 + 0.02;
    finalNoise = (finalNoise * perturb) + gl_PointCoord - 0.02;

    vec4 color = texture2D(u_TextureUnit, finalNoise);
    color = vec4(color.x*2.0, color.y*0.9, (color.y/color.x)*0.2, 1.0);
    finalNoise = clamp(finalNoise, 0.05, 1.0);
    color.w = texture2D(u_TextureUnit, finalNoise).z*2.0;
    color.w = color.w*texture2D(u_TextureUnit, gl_PointCoord).z;
    gl_FragColor = vec4(1,0,0,1) / color;

}