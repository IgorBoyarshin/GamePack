#version 330 core

layout (location = 0) out vec4 color;

in DATA {
	vec4 position;
	vec2 uv;
	float tid;
    vec4 color;
} fs_in;

uniform vec4 mainColor = vec4(1.0, 1.0, 1.0, 1.0);
uniform vec4 backgroundColor = vec4(0.0, 0.0, 0.0, 0.0);

uniform sampler2D textures[16];

void main()
{
	int tid = int(fs_in.tid - 0.5);
	vec4 texColor = texture(textures[tid], fs_in.uv);

	if (texColor == vec4(1.0, 1.0, 1.0, 1.0)) {
		if (backgroundColor.w == 0.0) {
			discard;
		} else {
			color = backgroundColor;
		}
	} else {
		color = fs_in.color;
		//color = mainColor;
	}
}