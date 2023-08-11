import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { MeterImportDetailComponent } from './meter-import-detail.component';

describe('MeterImport Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MeterImportDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: MeterImportDetailComponent,
              resolve: { meterImport: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(MeterImportDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load meterImport on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', MeterImportDetailComponent);

      // THEN
      expect(instance.meterImport).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
