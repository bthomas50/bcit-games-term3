/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sketchwars.graphics;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBVertexShader;
import org.lwjgl.opengl.GL11;

/**
 *
 * @author Najash Najimudeen <najash.najm@gmail.com>
 */
public class Shader {
    private int program = -1;
    
    public Shader(String vertexShaderFile, String fragmentShaderFile) throws Exception  {
        if (vertexShaderFile == null || vertexShaderFile.length() < 1) {
            throw new Exception("Invalid vertex shader file path.");
        } else if (fragmentShaderFile == null || fragmentShaderFile.length() < 1) {
            throw new Exception("Invalid fragment shader file path.");            
        }
        
        loadShader(vertexShaderFile, fragmentShaderFile);
    }
    
    private void loadShader(String vertexShaderFile, String fragmentShaderFile) throws Exception {
        int vertShader = createShaderFromFile(vertexShaderFile, ARBVertexShader.GL_VERTEX_SHADER_ARB);
        int fragShader = createShaderFromFile(fragmentShaderFile, ARBFragmentShader.GL_FRAGMENT_SHADER_ARB);
      
        int shaderProgram = ARBShaderObjects.glCreateProgramObjectARB();
         
        if(shaderProgram == 0) {
            throw new Exception("Error Creating shader program object.");
        }
            
        ARBShaderObjects.glAttachObjectARB(shaderProgram, vertShader);
        ARBShaderObjects.glAttachObjectARB(shaderProgram, fragShader);
         
        ARBShaderObjects.glLinkProgramARB(shaderProgram);
        if (ARBShaderObjects.glGetObjectParameteriARB(shaderProgram, ARBShaderObjects.GL_OBJECT_LINK_STATUS_ARB) == GL11.GL_FALSE) {
            throw new Exception(getLogInfo(shaderProgram));
        }
         
        ARBShaderObjects.glValidateProgramARB(shaderProgram);
        if (ARBShaderObjects.glGetObjectParameteriARB(shaderProgram, ARBShaderObjects.GL_OBJECT_VALIDATE_STATUS_ARB) == GL11.GL_FALSE) {
            throw new Exception(getLogInfo(shaderProgram));
        }
        
        program = shaderProgram;
    }
    
    private int createShaderFromString(String shaderString, int shaderType) throws Exception {
        int shader = 0;
        try {
            shader = ARBShaderObjects.glCreateShaderObjectARB(shaderType);
             
            if(shader == 0)
                return 0;
            
            if (shaderString != null) {
                ARBShaderObjects.glShaderSourceARB(shader, shaderString);
                ARBShaderObjects.glCompileShaderARB(shader);
                
                if (ARBShaderObjects.glGetObjectParameteriARB(shader, 
                        ARBShaderObjects.GL_OBJECT_COMPILE_STATUS_ARB) == GL11.GL_FALSE) {
                    throw new RuntimeException("Error creating shader: " + getLogInfo(shader));
                }
                return shader;
            }
        } catch(Exception exc) {
            ARBShaderObjects.glDeleteObjectARB(shader);
            throw exc;
        }
        return -1;
    }
    
    private int createShaderFromFile(String filename, int shaderType) throws Exception {
        String shaderFileStr = readFileAsString(filename);
            
        if (shaderFileStr != null) {
            return createShaderFromString(shaderFileStr, shaderType);
        }
        return -1;
    }
    
    private String readFileAsString(String filename) {
        StringBuilder source = new StringBuilder();
        BufferedReader reader= null;
        FileInputStream in = null;
        
        try {
            in = new FileInputStream(filename);
            reader = new BufferedReader(new InputStreamReader(in,"UTF-8"));
     
            String line;            
            while((line = reader.readLine()) != null) {
                source.append(line).append('\n');
            }
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            System.err.println("Shader:readFileAsString(): " + ex);
            return null;
        } catch (IOException ex) {
            System.err.println("Shader:readFileAsString(): " + ex);
            return null;
        }finally {
            try {
                if (reader != null)
                    reader.close();
                
                if (in != null)
                    in.close();
            } catch(Exception ex) {
                System.err.println("Shader:readFileAsString(): " + ex);
                return null;
            }
        }
       
        return source.toString();
    }

    
    private static String getLogInfo(int obj) {
        return ARBShaderObjects.glGetInfoLogARB(obj, ARBShaderObjects.glGetObjectParameteriARB(obj, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB));
    }

    public int getProgram() {
        return program;
    }

    public boolean isValid() {
        return program != -1;
    }

    public void begin() {
        if (program != -1) {
            ARBShaderObjects.glUseProgramObjectARB(program);
        }
    }

    public void end() {
        if (program != -1) {
            ARBShaderObjects.glUseProgramObjectARB(0);
        }
    }
}
