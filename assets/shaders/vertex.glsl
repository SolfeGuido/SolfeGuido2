attribute vec4 a_position;
attribute vec4 a_color;
attribute vec2 a_texCoord0;

uniform mat4 u_projTrans;
uniform vec2 u_viewport_size;

varying vec4 v_color;
varying vec2 v_texCoords;
varying vec2 v_position;

void main()
{
    v_color = a_color;
    v_texCoords = a_texCoord0;
    gl_Position =  u_projTrans * a_position;
    // https://stackoverflow.com/a/26969699/4281653
    vec3 ndc = gl_Position.xyz / gl_Position.w; //perspective divide/normalize
    vec2 viewportCoord = ndc.xy * 0.5 + 0.5; //ndc is -1 to 1 in GL. scale for 0 to 1
    v_position = viewportCoord * u_viewport_size;
}