var config = require('./build/webpack-dev-server-run.js')
var path = require('path')

module.exports = {
  entry: config.moduleName,
  output: {
    path: path.resolve('./bundle'),
    publicPath: '/build/',
    filename: 'bundle.js'
  },
  resolve: {
    modules: [ path.resolve('js'), path.resolve('..', 'src'), path.resolve('.'), path.resolve('node_modules') ],
    extensions: ['.js', '.css']
  },
  module: {
    loaders: [
      { test: /\.css$/, loader: 'style-loader!css-loader' }
    ]
  },
  devtool: '#source-map'
};

console.log(module.exports.resolve.modules);


