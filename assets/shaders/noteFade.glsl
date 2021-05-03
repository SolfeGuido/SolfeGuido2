#ifdef GL_ES
#define LOWP lowp
    precision mediump float;
#else
    #define LOWP
#endif

varying LOWP vec4 v_color;
varying vec2 v_texCoords;
varying vec2 v_position;

uniform sampler2D u_texture;

uniform float leftLimit;
uniform float rightLimit;
uniform float noteWidth;

void main()
{
    float left_alpha =  min( max(0.0, v_position.x - (leftLimit - noteWidth)) / noteWidth, 1.0);
    float right_alpha = min( max(0.0, rightLimit - v_position.x ) / noteWidth, 1.0);
    gl_FragColor = texture2D(u_texture, v_texCoords) * vec4(v_color.rgb, left_alpha * right_alpha * v_color.a);
}