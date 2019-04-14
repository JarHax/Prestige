package com.jarhax.prestige.client;

import com.jarhax.prestige.events.ClientEventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import org.apache.commons.io.IOUtils;
import org.lwjgl.opengl.*;

import java.io.InputStream;
import java.util.*;

public class ShaderHandler {
    
    
    public static final int VERT = ARBVertexShader.GL_VERTEX_SHADER_ARB;
    public static final int FRAG = ARBFragmentShader.GL_FRAGMENT_SHADER_ARB;
    public static int SCREEN;
    public static int STAR;
    public static int RECTS;
    public static int SQUIGGLE_STAR;
    public static int FBM;
    public static int MAZE;
    public static int LAVA_LAMP;
    public static int WAVES;
    
    
    public static final List<Integer> SHADERS = new ArrayList<>();
    
    public static void registerShaders() {
        try {
            SCREEN = create("/assets/prestige/shaders/screen");
            STAR = create("/assets/prestige/shaders/star");
            RECTS = create("/assets/prestige/shaders/rects");
            SQUIGGLE_STAR = create("/assets/prestige/shaders/squigglestar");
            FBM = create("/assets/prestige/shaders/fbm");
            MAZE = create("/assets/prestige/shaders/maze");
            LAVA_LAMP = create("/assets/prestige/shaders/lava_lamp");
            WAVES = create("/assets/prestige/shaders/waves");
    
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void unregisterShaders() {
        for(Integer shader : SHADERS) {
            deleteShader(shader);
        }
        SHADERS.clear();
    }
    
    public static void deleteShader(int id) {
        if(id != 0) {
            OpenGlHelper.glDeleteProgram(id);
        }
    }
    
    public static void useShader(int shader, Map<String, Object> data) {
        useShader(shader, Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight, data);
    }
    
    
    public static void useShader(int shader, int width, int height, Map<String, Object> data) {
        if(!useShaders())
            return;
        
        ARBShaderObjects.glUseProgramObjectARB(shader);
        
        if(shader != 0) {
            int time = ARBShaderObjects.glGetUniformLocationARB(shader, "time");
            ARBShaderObjects.glUniform1fARB(time, ClientEventHandler.totalTime);
            
            
            int res = ARBShaderObjects.glGetUniformLocationARB(shader, "resolution");
            ARBShaderObjects.glUniform3fARB(res, width, height,0);
            
            for(Map.Entry<String, Object> entry : data.entrySet()) {
                int id = ARBShaderObjects.glGetUniformLocationARB(shader, entry.getKey());
                if(entry.getValue() instanceof Float) {
                    ARBShaderObjects.glUniform1fARB(id, (Float) entry.getValue());
                }
                if(entry.getValue() instanceof Integer) {
                    ARBShaderObjects.glUniform1iARB(id, (Integer) entry.getValue());
                }
            }
            
            
        }
    }
    
    
    public static void userShader(int shader) {
        useShader(shader, new HashMap<>());
    }
    
    public static boolean useShaders() {
        return OpenGlHelper.shadersSupported;
    }
    
    public static void releaseShader() {
        useShader(0, new HashMap<>());
    }
    
    
    public static int create(String location) {
        int vertShader = 0;
        int fragShader = 0;
        
        int program = 0;
        try {
            vertShader = createShader(location + ".vert", VERT);
            
        } catch(Exception exc) {
            exc.printStackTrace();
            return 0;
        } finally {
        }
        
        
        try {
            fragShader = createShader(location + ".frag", FRAG);
        } catch(Exception exc) {
            exc.printStackTrace();
            return 0;
        } finally {
        }
        
        
        program = ARBShaderObjects.glCreateProgramObjectARB();
        
        if(program == 0)
            return 0;
        
        /*
         * if the vertex and fragment shaders setup sucessfully,
         * attach them to the shader program, link the sahder program
         * (into the GL context I suppose), and validate
         */
        if(vertShader != 0)
            ARBShaderObjects.glAttachObjectARB(program, vertShader);
        if(fragShader != 0)
            ARBShaderObjects.glAttachObjectARB(program, fragShader);
        
        ARBShaderObjects.glLinkProgramARB(program);
        if(ARBShaderObjects.glGetObjectParameteriARB(program, ARBShaderObjects.GL_OBJECT_LINK_STATUS_ARB) == GL11.GL_FALSE) {
            System.err.println(getLogInfo(program));
            return 0;
        }
        
        ARBShaderObjects.glValidateProgramARB(program);
        if(ARBShaderObjects.glGetObjectParameteriARB(program, ARBShaderObjects.GL_OBJECT_VALIDATE_STATUS_ARB) == GL11.GL_FALSE) {
            System.err.println(getLogInfo(program));
            return 0;
        }
        
        SHADERS.add(program);
        return program;
    }
    
    private static int createShader(String filename, int shaderType) throws Exception {
        int shader = 0;
        try {
            shader = ARBShaderObjects.glCreateShaderObjectARB(shaderType);
            
            if(shader == 0)
                return 0;
            
            InputStream stream = ShaderHandler.class.getResourceAsStream(filename);
            if(stream == null) {
                return 0;
            }
            String string = IOUtils.toString(stream);
            ARBShaderObjects.glShaderSourceARB(shader, string);
            ARBShaderObjects.glCompileShaderARB(shader);
            
            if(ARBShaderObjects.glGetObjectParameteriARB(shader, ARBShaderObjects.GL_OBJECT_COMPILE_STATUS_ARB) == GL11.GL_FALSE)
                throw new RuntimeException("Error creating shader: " + getLogInfo(shader));
            
            return shader;
        } catch(Exception exc) {
            ARBShaderObjects.glDeleteObjectARB(shader);
            throw exc;
        }
    }
    
    
    private static String getLogInfo(int obj) {
        return ARBShaderObjects.glGetInfoLogARB(obj, ARBShaderObjects.glGetObjectParameteriARB(obj, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB));
    }
    
    
}
