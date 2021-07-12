// adapted from http://devlog-martinsh.blogspot.com/2011/03/glsl-8x8-bayer-matrix-dithering.html

uniform sampler2D tDiffuse
// uniform sampler2D bayerTexture;

varying vec2 vUv;

float find_closest(float x, float y, float c0) {
    float limit = texture2D(bayerTexture, vec2(x / 4.0, y / 4.0)).r;

    if(c0 < limit)
        return 0.0;
    else
        return 1.0;
}

void main(void) {
    // vec4 lum = vec4(0.5, 0.5, 0.5, 0);

    // float grayscale = dot(texture2D(bgl_RenderedTexture, vUv), lum);
    // vec3 rgb = texture2D(bgl_RenderedTexture, vUv).rgb;

    // vec2 xy = vUv;
    // float x = mod(xy.x, 4.0);
    // float y = mod(xy.y, 4.0);

    // float final = find_closest(x, y, grayscale);
    gl_FragColor = vec4(1.0, 1.0, 1.0, 1.0);
}