import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../property-type.test-samples';

import { PropertyTypeFormService } from './property-type-form.service';

describe('PropertyType Form Service', () => {
  let service: PropertyTypeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PropertyTypeFormService);
  });

  describe('Service methods', () => {
    describe('createPropertyTypeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPropertyTypeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            pattern: expect.any(Object),
          })
        );
      });

      it('passing IPropertyType should create a new form with FormGroup', () => {
        const formGroup = service.createPropertyTypeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            pattern: expect.any(Object),
          })
        );
      });
    });

    describe('getPropertyType', () => {
      it('should return NewPropertyType for default PropertyType initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createPropertyTypeFormGroup(sampleWithNewData);

        const propertyType = service.getPropertyType(formGroup) as any;

        expect(propertyType).toMatchObject(sampleWithNewData);
      });

      it('should return NewPropertyType for empty PropertyType initial value', () => {
        const formGroup = service.createPropertyTypeFormGroup();

        const propertyType = service.getPropertyType(formGroup) as any;

        expect(propertyType).toMatchObject({});
      });

      it('should return IPropertyType', () => {
        const formGroup = service.createPropertyTypeFormGroup(sampleWithRequiredData);

        const propertyType = service.getPropertyType(formGroup) as any;

        expect(propertyType).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPropertyType should not enable id FormControl', () => {
        const formGroup = service.createPropertyTypeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPropertyType should disable id FormControl', () => {
        const formGroup = service.createPropertyTypeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
