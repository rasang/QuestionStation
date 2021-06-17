module.exports = {
	pages: {
		index:{
			entry: 'src/main.js',
			template: 'public/index.html',
			filename: 'index.html'
		},
		details:{
			entry: 'src/details.js',
			template: 'public/details.html',
			filename: 'details.html'
		},
		admin:{
			entry: 'src/admin.js',
			template: 'public/admin.html',
			filename: 'admin.html'
		}
	}
}