#version 330 core

layout (location = 0) out vec4 color;

uniform vec2 light_pos;
uniform float r = 0.0;

in DATA {
	vec4 position;
	vec2 uv;
	float tid;
    vec4 color;
} fs_in;

uniform sampler2D textures[16];

void main()
{
	float intensity = 1.0;

	if (r == 1.0) {
		intensity = 3.0 / length(fs_in.position.xy - light_pos);
		//color = fs_in.color * intensity;
		//color = texture(tex, fs_in.uv) * intensity;
	} else if (r == 2.0){
		intensity = 3.0 / length(fs_in.position.xy - light_pos) / length(fs_in.position.xy - light_pos);
		//color = fs_in.color * intensity;
		//color = texture(tex, fs_in.uv * intensity);
	} else if (r == 0.0){
		intensity = 1.0;
		//color = fs_in.color;
		//color = texture(tex, fs_in.uv) * intensity;
	}

	vec4 texColor = fs_in.color;
	if (fs_in.tid > 0.0) {
		int tid = int(fs_in.tid - 0.5);
		texColor = texture(textures[tid], fs_in.uv);
	}

	color = texColor * intensity;
}