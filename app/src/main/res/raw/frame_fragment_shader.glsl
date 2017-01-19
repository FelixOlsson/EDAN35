varying vec2 TexCoords;

uniform sampler2D screenTexture;
uniform sampler2D blurrTexture;

vec3 fiveTimesFiveBlurEffect();
vec3 threeTimesThree();

void main()
{

       const float gamma = 2.2;
       const float exposure = 0.7;
       vec3 color = texture2D(screenTexture, TexCoords).rgb;
       vec3 bloomColor = fiveTimesFiveBlurEffect();
       color += bloomColor;
       vec3 result = pow(vec3(1.0) - exp(-color * exposure),vec3(1.0 / gamma));
       gl_FragColor = vec4(result, 1.0f);

}

vec3 fiveTimesFiveBlurEffect()
{
    float offset = 1.0 / 200.0;
    vec2 offsets[] = vec2[25](
                    // First row
                    vec2(-offset, offset),
                    vec2(-(offset/2.0f), offset),
                    vec2(0.0f,    offset),
                    vec2(offset/2.0f, offset),
                    vec2(offset,  offset),
                    // Second row
                    vec2(-offset, offset/2.0f),
                    vec2(-(offset/2.0f), offset/2.0f),
                    vec2(0.0f,    offset/2.0f),
                    vec2(offset/2.0f, offset/2.0f),
                    vec2(offset,  offset/2.0f),

                    // Third row
                    vec2(-offset, 0.0f),
                    vec2(-(offset/2.0f), 0.0f),
                    vec2(0.0f,    0.0f),
                    vec2(offset/2.0f, 0.0f),
                    vec2(offset,  0.0f),

                    // Fourth row
                    vec2(-offset, -offset/2.0f),
                    vec2(-(offset/2.0f), -offset/2.0f),
                    vec2(0.0f,    -offset/2.0f),
                    vec2(offset/2.0f, -offset/2.0f),
                    vec2(offset,  -offset/2.0f),

                    // Firth row
                     vec2(-offset, -offset),
                     vec2(-(offset/2.0f), -offset),
                     vec2(0.0f,    -offset),
                     vec2(offset/2.0f, -offset),
                     vec2(offset,  -offset)
                );

                 float kernel[] = float[25](
                                     1.0 / 256.0, 4.0 / 256.0,  6.0 / 256.0,  4.0 / 256.0,  1.0 / 256.0,
                                     4.0 / 256.0, 16.0 / 256.0, 24.0 / 256.0, 16.0 / 256.0, 4.0 / 256.0,
                                     6.0 / 256.0, 24.0 / 256.0, 36.0 / 256.0, 24.0 / 256.0, 6.0 / 256.0,
                                     4.0 / 256.0, 16.0 / 256.0, 24.0 / 256.0, 16.0 / 256.0, 4.0 / 256.0,
                                     1.0 / 256.0, 4.0 / 256.0,  6.0 / 256.0,  4.0 / 256.0,  1.0 / 256.0
                                 );
                        // Don´t ask me why i needed to do two loops, it just would not work otherwise
                        vec3 tempTex[25];
                        for(int i = 0; i < 17; i++)
                        {
                            tempTex[i] = vec3(texture2D(blurrTexture, TexCoords.st + offsets[i]));
                        }

                        for(int i = 17; i < 25; i++)
                        {
                           tempTex[i] = vec3(texture2D(blurrTexture, TexCoords.st + offsets[i]));
                        }

                        vec3 col = vec3(0.0);

                        for(int i = 0; i < 17; i++) {
                            col += tempTex[i] * kernel[i];
                        }

                        for(int i = 17; i < 25; i++) {
                            col += tempTex[i] * kernel[i];
                        }

                        return col;
}

vec3 threeTimesThree() {

  float offset = 1.0 / 300.0;
  vec2 offsets[] = vec2[9](
            vec2(-offset, offset),
            vec2(0.0f,    offset),
            vec2(offset,  offset),
            vec2(-offset, 0.0f),
            vec2(0.0f,    0.0f),
            vec2(offset,  0.0f),
            vec2(-offset, -offset),
            vec2(0.0f,    -offset),
            vec2(offset,  -offset)
        );
        float kernel[] = float[9](
            -1.0, -1.0, -1.0,
            -1.0,  8.0, -1.0,
            -1.0, -1.0, -1.0
        );

        /*float kernel[] = float[9](
            1.0 / 16.0, 2.0 / 16.0, 1.0 / 16.0,
            2.0 / 16.0, 4.0 / 16.0, 2.0 / 16.0,
            1.0 / 16.0, 2.0 / 16.0, 1.0 / 16.0
        );*/

        /*float kernel[] = float[9](
                    0.0, -1.0, 0.0,
                    -1.0,  5.0, -1.0,
                    0.0, -1.0, 0.0
         );*/

        vec3 tempTex[9];
        for(int i = 0; i < 9; i++)
        {
            tempTex[i] = vec3(texture2D(blurrTexture, TexCoords.st + offsets[i]));
        }


        vec3 col = vec3(0.0);
        for(int i = 0; i < 9; i++) {
            col += tempTex[i] * kernel[i];
        }

        return col;
}