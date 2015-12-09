
varying vec4 v_color;
varying vec2 v_texCoords;

void main () {
	v_color =  vec4 (1, 1, 1, 1);
	 
	v_texCoords = gl_MultiTexCoord0.st;
	
	gl_Position = gl_ModelViewProjectionMatrix*gl_Vertex;
}