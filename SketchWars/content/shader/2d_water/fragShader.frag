#ifdef GL_ES
 
precision medimp float;
 
#endif
 
varying vec4 v_color;
 
varying vec2 v_texCoords;
 
uniform sampler2D u_texture;

uniform float wave_angle;
uniform float wave_length;
uniform float amplitude; 
uniform float y_offset;
 
void main (){
	float t = v_texCoords.y -y_offset + (sin (v_texCoords.x * wave_length+wave_angle) * amplitude);
	
	gl_FragColor = v_color * texture2D(u_texture, vec2(v_texCoords.x, t));
}