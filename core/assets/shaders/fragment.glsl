#version 120

varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform float time;
uniform float px;
uniform float py;

void main() {
  gl_FragColor = texture2D(u_texture, v_texCoords);

    float dst = sqrt((v_texCoords.x - px) * (v_texCoords.x - px)
                + (v_texCoords.y - py) * (v_texCoords.y - py));

   if (dst > 0.2f){
         gl_FragColor.r *= (1.0f - (dst - 0.2) * 0.7f);
         gl_FragColor.g *= (1.0f - (dst - 0.2) * 0.7f) ;
         gl_FragColor.b *= (1.0f - (dst - 0.2) * 0.7f) ;
     }
}
