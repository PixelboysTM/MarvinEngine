#type vertex
#version 330 core
layout (location=0) in vec3 aPos;
layout (location=1) in vec4 aColor;
layout (location=2) in vec4 aOutline;

uniform mat4 uProj;
uniform mat4 uView;


out vec4 fColor;
out vec4 fOutline;
out vec2 fposI;
flat out vec2 fposO;

void main(){
    fColor = aColor;
    fOutline = aOutline;
    vec4 pos = uProj * uView * vec4(aPos,1.0);
    fposI = vec2(pos.x,pos.y);
    fposO = vec2(pos.x,pos.y);
    gl_Position = pos;
}

#type fragment
#version 330 core

in vec4 fColor;
in vec4 fOutline;
in vec2 fposI;
flat in vec2 fposO;

out vec4 color;

void main(){
        float divX = sqrt((fposO.x - fposI.x) * (fposO.x - fposI.x));
        float divY = sqrt((fposO.y - fposI.y) * (fposO.y - fposI.y));
        if  ( divX < 0.05f || divY < 0.05f ){
            color = fOutline;
        }else{
            color = fColor;
        }

}