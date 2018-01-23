config.module.rules.push(
    {
        test: /\.svg$/,
        exclude: /node_modules/,
        loader: 'svg-react-loader'
    }
);