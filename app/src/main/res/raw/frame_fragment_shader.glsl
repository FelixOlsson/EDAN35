varying vec2 TexCoords;

uniform sampler2D screenTexture;
uniform sampler2D blurrTexture;


void main()
{
    float offset = 1.0 / 100.0;

    vec2 offsets[] = vec2[9](
            vec2(-offset, offset),  // top-left
            vec2(0.0f,    offset),  // top-center
            vec2(offset,  offset),  // top-right
            vec2(-offset, 0.0f),    // center-left
            vec2(0.0f,    0.0f),    // center-center
            vec2(offset,  0.0f),    // center-right
            vec2(-offset, -offset), // bottom-left
            vec2(0.0f,    -offset), // bottom-center
            vec2(offset,  -offset)  // bottom-right
        );

        /*float kernel[] = float[9](
            -1.0, -1.0, -1.0,
            -1.0,  8.0, -1.0,
            -1.0, -1.0, -1.0
        );*/

        float kernel[] = float[9](
            1.0 / 16.0, 2.0 / 16.0, 1.0 / 16.0,
            2.0 / 16.0, 4.0 / 16.0, 2.0 / 16.0,
            1.0 / 16.0, 2.0 / 16.0, 1.0 / 16.0
        );

        /*float kernel[] = float[9](
                    0.0, -1.0, 0.0,
                    -1.0,  5.0, -1.0,
                    0.0, -1.0, 0.0
         );*/

        vec3 sampleTex[9];
        for(int i = 0; i < 9; i++)
        {
            sampleTex[i] = vec3(texture2D(blurrTexture, TexCoords.st + offsets[i]));
        }


        vec3 col = vec3(0.0);
        for(int i = 0; i < 9; i++)
            col += sampleTex[i] * kernel[i];

       const float gamma = 2.2;
       const float exposure = 0.3;
       vec3 hdrColor = texture2D(screenTexture, TexCoords).rgb;
       vec3 bloomColor = col;
       hdrColor += bloomColor;
       vec3 result = vec3(1.0) - exp(-hdrColor * exposure);
       result = pow(result, vec3(1.0 / gamma));
       gl_FragColor = vec4(result, 1.0f);


        //gl_FragColor = vec4(hdrColor, 0.1f);




}