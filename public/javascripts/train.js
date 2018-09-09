
let upPresses = []
let downPresses = []

let getMean = function(arr){

    let sum = 0
    for(let i = 0; i<arr.length; i++){
        sum = sum + arr[i]
    }

    return sum/arr.length
}

let standardize = function(arr){
    let mean = getMean(arr)
    let standardized = []
    for(let i = 0; i < arr.length; i++){
        standardized.push(arr[i]/mean)
    }

    return standardized
}

$('#typed_word').keydown(function(){
        downPresses.push(window.performance.now())
    }
);

$('#typed_word').keyup(function(){
        upPresses.push(window.performance.now())
    }
);

$('#submit_train_data').click(function(){
    let features = []
    for(let i = 0; i < upPresses.length; i++){
        features.push(upPresses[i] - downPresses[i])
        if(i + 1 < upPresses.length){
            features.push(downPresses[i + 1] - upPresses[i])
        }
    }

    $.ajax({
      type: "POST",
      url: '/train',
      data: {
        "required_word": $('#required_word').val(),
        "typed_word": $('#typed_word').val(),
        "features": features
      }
    });

    console.log(standardizedFeatures)
});

$('#reset_train_data').click(function(){
    upPresses = []
    downPresses = []
});
