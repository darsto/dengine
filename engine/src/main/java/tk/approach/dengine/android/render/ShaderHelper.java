package tk.approach.dengine.android.render;

import android.opengl.GLES20;

public class ShaderHelper {

    /* SHADER Gui
    *
    * This shader is for rendering 2D text textures straight from a texture
    * Color and alpha blended.
    *
    */
    public static final String vs_Gui
            = "precision mediump float;"
            + "uniform mat4 uMVPMatrix;"
            + "attribute vec4 vPosition;"
            + "attribute vec3 a_Color;"
            + "attribute vec2 a_texCoord;"
            + "varying vec3 v_Color;"
            + "varying vec2 v_texCoord;"
            + "void main() {"
            + "  gl_Position = uMVPMatrix * vPosition;"
            + "  v_texCoord = a_texCoord;"
            + "  v_Color = a_Color;"
            + "}";
    public static final String fs_Gui
            = "precision mediump float;"
            + "varying vec3 v_Color;"
            + "varying vec2 v_texCoord;"
            + "uniform sampler2D s_texture;"
            + "uniform float alpha;"
            + "uniform float texture_u;"
            + "uniform float texture_v;"
            + "void main() {"
            + "  gl_FragColor = texture2D( s_texture, vec2( v_texCoord.x + texture_u, v_texCoord.y + texture_v ) ) * vec4(v_Color, alpha);"
            + "  gl_FragColor.rgb *= alpha;"
            + "}";

    public static int loadShader(int type, String shaderCode) {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }

}
