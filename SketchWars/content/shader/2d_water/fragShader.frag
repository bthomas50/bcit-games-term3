#ifdef GL_ES
 
precision medimp float;
 
#endif
 
varying vec4 v_color;
 
varying vec2 v_texCoords;
 
uniform sampler2D u_texture;
 
uniform sampler2D u_texture_displacement;
 
uniform float wave_angle;
uniform float wave_length;
uniform float amplitude; 
uniform float y_offset;
 
void main (){

	vec2 displacement = texture2D(u_texture_displacement, v_texCoords/6.0).xy;
	 
	float t = v_texCoords.y + ((displacement.y * 0.1) - y_offset) + (sin (v_texCoords.x * wave_length+wave_angle) * amplitude);
	 
	gl_FragColor = v_color * texture2D(u_texture, vec2(v_texCoords.x, t));

}