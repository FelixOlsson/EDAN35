precision mediump float;
//uniform vec4 vColor;
varying vec4 test;


void main() {
   // vColor.rbg += rand(vColor.rb);
    //vColor -= 0.1;
    gl_FragColor = test;
}

float rand(vec2 co){
  return fract(sin(dot(co.xy ,vec2(12.9898,78.233))) * 43758.5453);
}
