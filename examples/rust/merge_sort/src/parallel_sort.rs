use std::thread;
use crate::merge::merge;

pub fn sort(array: &[i32]) -> Vec<i32> {
    let len = array.len();
    if len <= 1 {
        array.to_vec()
    } else if len < 10_000{
        crate::serial_sort::sort(array)
    }
    else {
        let (x, y) = thread::scope(|s| {
            let x  = s.spawn(||sort(&array[..len/2]));
            let y = s.spawn(||sort(&array[len/2..]));
            (x.join().unwrap(), y.join().unwrap())
        });
        merge(&x, &y)
    }
}