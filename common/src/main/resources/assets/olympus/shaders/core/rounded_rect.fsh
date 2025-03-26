#version 150

in vec4 vertexColor;

uniform vec4 ColorModulator;

uniform vec4 borderColor;
uniform vec4 borderRadius;
uniform float borderWidth;
uniform vec2 size;
uniform vec2 center;
uniform float scaleFactor;

out vec4 fragColor;

// From: https://iquilezles.org/articles/distfunctions2d/
float sdRoundedBox(vec2 p, vec2 b, vec4 r){
    r.xy = (p.x > 0.0) ? r.xy : r.zw;
    r.x  = (p.y > 0.0) ? r.x  : r.y;
    vec2 q = abs(p)-b+r.x;
    return min(max(q.x,q.y),0.0) + length(max(q,0.0)) - r.x;
}

void main() {
    if (vertexColor.a == 0.0) {
        discard;
    }

    vec2 halfSize = size / 2.0;
    float distance = sdRoundedBox(gl_FragCoord.xy - center, halfSize, borderRadius * scaleFactor);
    float smoothed = min(1.0 - distance, vertexColor.a);
    float border = min(1.0 - smoothstep(borderWidth, borderWidth, abs(distance)), borderColor.a);

    if (border > 0.0) {
        fragColor = borderColor * vec4(1.0, 1.0, 1.0, border) * ColorModulator;
    } else {
        fragColor = vertexColor * vec4(1.0, 1.0, 1.0, smoothed) * ColorModulator;
    }
}
