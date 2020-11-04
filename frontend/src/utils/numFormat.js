const digitsRE = /(\d{3})(?=\d)/g;

export default (value, currency = '', decimals = 0) => {
    value = parseFloat(value);
    if (!value && value !== 0) return '';
    const stringified = Math.abs(value).toFixed(decimals);
    const $int = decimals ? stringified.slice(0, -1 - decimals) : stringified;
    const i = $int.length % 3;
    const head = i > 0 ? ($int.slice(0, i) + ($int.length > 3 ? ',' : '')) : '';
    const $float = decimals ? stringified.slice(-1 - decimals) : '';
    const sign = value < 0 ? '-' : '';
    return `${sign}${head}${$int.slice(i).replace(digitsRE, '$1,')}${$float} ${currency}`;
};
