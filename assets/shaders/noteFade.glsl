#ifdef GL_ES
precision highp float;
#endif

attribute vec4 a_position;
attribute vec4 a_color;
attribute vec2 a_texCoord0;

uniform float leftLimit;
uniform float rightLimit;
uniform float noteWidth;

void main()
{
    float left_alpha =  min( max(0.0, a_position.x - (leftLimit - noteWidth)) / noteWidth, 1.0);
    float right_alpha = min( max(0.0, rightLimit - a_position.x ) / noteWidth, 1.0);
    gl_FragColor = texture2D(sceneTex, a_texCoord0) * vec4(color.rgb, leftAlpha * rightAlpha * color.a);

}