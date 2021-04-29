#ifdef GL_ES
precision highp float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;
varying vec4 v_position;

uniform sampler2D u_texture;

uniform float leftLimit;
uniform float rightLimit;
uniform float noteWidth;

void main()
{
//    float left_alpha =  min( max(0.0, v_position.x - (leftLimit - noteWidth)) / noteWidth, 1.0);
//    float right_alpha = min( max(0.0, rightLimit - v_position.x ) / noteWidth, 1.0);
//    gl_FragColor = texture2D(sceneTex, a_texCoord0) * vec4(a_color.rgb, leftAlpha * rightAlpha * a_color.a);
//    gl_FragColor = vec4(v_color.rgb, left_alpha * right_alpha * v_color.a);
    gl_FragColor = vec4(texture2D(u_texture, v_texCoords));
}